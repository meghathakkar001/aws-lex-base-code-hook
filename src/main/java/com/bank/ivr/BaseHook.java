package com.bank.ivr;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.LexEvent;
import com.amazonaws.util.StringUtils;
import com.bank.ivr.model.DialogAction;
import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Message;
import com.bank.ivr.model.Prerequisite;
import com.bank.ivr.model.Slot;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseHook {

	private AWSLambda lambdaClient;

	protected void setIntent(Intent intent) {
		this.intent = intent;
	}

	/**
	 * Check if RFC is set, if not set RFC For given RFC intent in session,
	 * check pre-requisites list with status in session If not satisfied, switch
	 * intent to the first unsatisfied intent If required slots are not filled,
	 * start filling them one after another as per configuration, give code hook
	 * for this Once all slots are filled, implement the fulfillment function as
	 * per code hook After fulfillment function, check if RFC is this intent or
	 * any other intent: - If this intent, do next steps as per config:
	 * ElicitIntent, Terminate, Transfer implemented for now
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	private Intent intent;
	private String rfc;
	private String rfcCodeHookAlias;
	private List<String> intentsFulfilled;
	private boolean intentAcknowledged;

	public LexResponse invokeHook(LexEvent request, Context context) {
        initialize(request);

        System.out.println(intent.getIntentName()+" invoked with request: "+request);
        
        boolean disambStatus = isIntentDisamb(intent);
        LexResponse lexResponse=null;
        if(disambStatus){
        	Slot mandatorySlot = intent.getMandatorySlots().get(0);
        	String slotValue = request.getCurrentIntent().getSlots()
					.get(mandatorySlot.getSlotName());
			System.out.println(intent.getIntentName() + ": Slot value for "
					+ mandatorySlot.getSlotName() + " is: " + slotValue);
			if(!StringUtils.isNullOrEmpty(slotValue)){
				String functionName = getLambdaFunctionName(slotValue);
				if(!StringUtils.isNullOrEmpty(functionName)){
					lexResponse = switchIntent(request,functionName);
					if(lexResponse!=null){
						return lexResponse; 
					}
					//if lexResponse if null at this stage then it is a NM/NI case. Need to handle.
				}
			}
        }
        
        if(!isRFCSet() && !disambStatus){
            rfc=this.intent.getIntentName();
            request.getSessionAttributes().put("rfc",rfc);
            request.getSessionAttributes().put("rfcCodeHookAlias",intent.getIntentAlias());
            System.out.println(intent.getIntentName()+" RFC set: "+rfc);
        }

        lexResponse= checkPrerequisites(request,context);
        if(lexResponse!=null){
            System.out.println(intent.getIntentName()+" Prerequisite response: "+lexResponse);
            return lexResponse;
        }

        lexResponse= fillMandatorySlots(request);
        if(lexResponse!=null){

            System.out.println(intent.getIntentName()+" Slot filling response: "+lexResponse);
            return lexResponse;
        }

        lexResponse=fulfillIntent();

        intentsFulfilled.add(intent.getIntentName());
        String intentsFulfilledString=request.getSessionAttributes().get("intentsFulfilled");
        if(StringUtils.isNullOrEmpty(intentsFulfilledString)){
            request.getSessionAttributes().put("intentsFulfilled",intent.getIntentName());

        }else {
            request.getSessionAttributes().put("intentsFulfilled", intentsFulfilledString + "," + intent.getIntentName());
        }

        if(lexResponse!=null){
            System.out.println(intent.getIntentName()+" Fulfillment response: "+lexResponse);
            return lexResponse;
        }

        lexResponse=nextAction(request);
        System.out.println(intent.getIntentName()+" Next action response: "+lexResponse);

        return lexResponse;

    }

	private String getLambdaFunctionName(String intentName) {
		//JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String lambdaFunctionName=null;
        try (FileReader reader = new FileReader("intentDefinition.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            lambdaFunctionName = parseIntentObject( (JSONObject) obj , intentName);
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lambdaFunctionName;
	}
	
	private static String parseIntentObject(JSONObject intentObject, String intentName) 
    {    
		
		JSONObject intentObj = (JSONObject) intentObject.get(intentName);
        //Get lambda function name
        String lambdaFunctionName = (String) intentObj.get("lambda_function_name");    
        System.out.println(lambdaFunctionName);
        return lambdaFunctionName;

    }

	private boolean isIntentDisamb(Intent intent) {

		int slotSize = intent.getMandatorySlots().size();

		if (slotSize == 1) {
			Slot mandatorySlot = intent.getMandatorySlots().get(0);
			String slotName = mandatorySlot.getSlotName();
			if (slotName.startsWith("Disamb") || slotName.startsWith("disamb")) {
				return true;
			}
		}
		return false;
	}

	private LexResponse switchIntent(LexEvent request,String functionName){
    	
    	// (2) Instantiate AWSLambdaClientBuilder to build the Lambda client
    	AWSLambdaClientBuilder builder = AWSLambdaClientBuilder.standard();
    	// (3) Build the client, which will ultimately invoke the function
    	AWSLambda client = builder.build();
    	// (4) Create an InvokeRequest with required parameters
    	InvokeRequest req = new InvokeRequest().withInvocationType(InvocationType.RequestResponse)
    	                           .withFunctionName(functionName).
    	                           withPayload(createLambdaPayload(request));
    	// (5) Invoke the function and capture response
    	InvokeResult result = client.invoke(req);
    	
    	System.out.println("Switch intent result "+result);
    	System.out.println("Switch intent payload " + StandardCharsets.UTF_8.decode(result.getPayload()).toString());
    	System.out.println("Switch intent payload array " + result.getPayload().array());
    	
    	ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(result.getPayload().array(),LexResponse.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	
	private String createLambdaPayload(LexEvent lexRequest) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(lexRequest);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private LexResponse nextAction(LexEvent request) {
		LexResponse lexResponse = new LexResponse();
		Map<String, String> sessionAttributes = request.getSessionAttributes();
		if (!rfc.equals(intent.getIntentName())) {
			return switchIntent(request,getLambdaFunctionName(rfc));
			/*request.getCurrentIntent().setName(rfc);

			final AmendBankDetailsCodeHook amendBankDetailsCodeHook = LambdaInvokerFactory
					.builder()
					.lambdaClient(AWSLambdaClientBuilder.defaultClient())
					.functionAlias(rfcCodeHookAlias)
					.build(AmendBankDetailsCodeHook.class);
			return amendBankDetailsCodeHook.invokeHook(request);*/
			/*
			 * AmazonLexRuntime client =
			 * AmazonLexRuntimeClientBuilder.defaultClient();
			 * com.amazonaws.services.lexruntime.model.DialogAction dialogAction
			 * = new
			 * com.amazonaws.services.lexruntime.model.DialogAction().withIntentName
			 * (rfc).withType(DialogActionType.ConfirmIntent).withMessage(
			 * "DOES NT MATTER").withMessageFormat(MessageFormatType.PlainText);
			 * PutSessionRequest putSessionRequest= new
			 * PutSessionRequest().withBotName
			 * ("ContactCenterBot").withBotAlias("$LATEST"
			 * ).withUserId(request.getUserId
			 * ()).withSessionAttributes(request.getSessionAttributes
			 * ()).withDialogAction(dialogAction);
			 * client.putSession(putSessionRequest);
			 * System.out.println("PutSession is successful: "
			 * +putSessionRequest);
			 * System.out.println("PutSession to original rfc:"+rfc);
			 */

		}

		System.out.println("No next action for intent:"
				+ intent.getIntentName());
		return finalRFCMessage();

	}

	protected abstract LexResponse finalRFCMessage();

	protected abstract LexResponse fulfillIntent();

	private LexResponse fillMandatorySlots(LexEvent request) {
		for (Slot mandatorySlot : intent.getMandatorySlots()) {
			String slotValue = request.getCurrentIntent().getSlots()
					.get(mandatorySlot.getSlotName());
			System.out.println(intent.getIntentName() + ": Slot value for "
					+ mandatorySlot.getSlotName() + " is: " + slotValue);
			if (StringUtils.isNullOrEmpty(slotValue)) {
				LexResponse lexResponse = new LexResponse();
				Map<String, String> sessionAttributes = request
						.getSessionAttributes();
				DialogAction dialogAction = new DialogAction();
				dialogAction.setIntentName(intent.getIntentName());
				dialogAction.setType("ElicitSlot");
				dialogAction.setSlotToElicit(mandatorySlot.getSlotName());
				Message message = new Message();
				message.setContentType("SSML");
				String slotPrompt= getSlotPrompt(request,mandatorySlot);
				message.setContent(slotPrompt);
				dialogAction.setMessage(message);

				lexResponse.setSessionAttributes(sessionAttributes);
				lexResponse.setDialogAction(dialogAction);
				return lexResponse;
			}
		}
		return null;
	}

	private String getSlotPrompt(LexEvent request,Slot mandatorySlot) {
		String acknowledgementPrompt=request.getSessionAttributes().get("acknowledgementPrompt");
		String slotPrompt="<speak>";
		System.out.println("Acknowledgement prompt is: "+acknowledgementPrompt);
		slotPrompt+=(acknowledgementPrompt != null) ? acknowledgementPrompt+mandatorySlot
				.getPrimaryPrompt() : mandatorySlot.getPrimaryPrompt();
		slotPrompt+="</speak>";
		request.getSessionAttributes().put("acknowledgementPrompt","");
		System.out.println("Acknowledgement prompt is set to empty");
		return slotPrompt;
	}

	protected LexResponse checkPrerequisites(LexEvent request, Context context) {
		List<Prerequisite> preRequisites = intent.getPreRequisites();
		for (Prerequisite preRequisiteIntent : preRequisites) {
			if (!intentsFulfilled.contains(preRequisiteIntent.getIntentName())) {
				
				return switchIntent(request,preRequisiteIntent.getLambdaCodeHookAlias());
				/*request.getCurrentIntent().setName(
						preRequisiteIntent.getIntentName());
				final IdentificationCodeHook identificationCodeHook = LambdaInvokerFactory
						.builder()
						.lambdaClient(AWSLambdaClientBuilder.defaultClient())
						.functionAlias(
								preRequisiteIntent.getLambdaCodeHookAlias())
						.build(IdentificationCodeHook.class);
				return identificationCodeHook.invokeHook(request);*/
				
			}
		}
		return null;
	}


	private boolean isRFCSet() {
		return !StringUtils.isNullOrEmpty(rfc);
	}

	private void initialize(LexEvent request) {
		initializeIntent();
		setRFC(request);
		setIntentsFulfilled(request);
		setIntentAcknowledged(request);
		initializeLambdaClient();

	}

	private void initializeLambdaClient() {

		lambdaClient = AWSLambdaClientBuilder.defaultClient();

	}

	protected abstract void initializeIntent();

	private void setIntentAcknowledged(LexEvent request) {
		String intentAcknowledgedString = request.getSessionAttributes().get(intent.getIntentName()+
				"-intentAcknowledged");
		boolean intentAcknowledged= "Yes".equals(intentAcknowledgedString);

		String acknowledgeString = request.getSessionAttributes().get(
				"acknowledgementPrompt");
		if (acknowledgeString==null){
			acknowledgeString="";
		}
		if (!intentAcknowledged && intent.isAcknowledgeIntent() ) {
			request.getSessionAttributes().put("acknowledgementPrompt",acknowledgeString+intent.getAcknolwegementPrompt());
			System.out.println("acknowledgementPrompt set to: "+request.getSessionAttributes().get("acknowledgementPrompt"));
			request.getSessionAttributes().put(intent.getIntentName()+"-intentAcknowledged", "Yes");
		}else {
			System.out.println("Intent does not need to be acknowledged:"+ intent.getIntentName());
		}
	}

	private void setIntentsFulfilled(LexEvent request) {

		intentsFulfilled = new ArrayList<String>();
		String intentsString = request.getSessionAttributes().get(
				"intentsFulfilled");
		if (intentsString != null) {
			String[] splittedString = intentsString.split(",");
			for (String intentFulfilled : splittedString) {
				intentsFulfilled.add(intentFulfilled);
			}
		}
	}

	private void setRFC(LexEvent request) {
		this.rfc = request.getSessionAttributes().get("rfc");
		this.rfcCodeHookAlias = request.getSessionAttributes().get(
				"rfcCodeHookAlias");
	}

}
