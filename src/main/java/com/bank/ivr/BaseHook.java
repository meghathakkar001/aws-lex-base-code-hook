package com.bank.ivr;


import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.LexEvent;
import com.amazonaws.services.lexruntime.AmazonLexRuntime;
import com.amazonaws.services.lexruntime.AmazonLexRuntimeClientBuilder;
import com.amazonaws.services.lexruntime.model.DialogActionType;
import com.amazonaws.services.lexruntime.model.MessageFormatType;
import com.amazonaws.services.lexruntime.model.PutSessionRequest;
import com.amazonaws.util.StringUtils;
import com.bank.ivr.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseHook {

    private AWSLambda lambdaClient;

    protected void setIntent(Intent intent) {
        this.intent = intent;
    }

    /**
     * Check if RFC is set, if not set RFC
     * For given RFC intent in session, check pre-requisites list with status in session
     * If not satisfied, switch intent to the first unsatisfied intent
     * If required slots are not filled, start filling them one after another as per configuration, give code hook for this
     * Once all slots are filled, implement the fulfillment function as per code hook
     * After fulfillment function, check if RFC is this intent or any other intent:
     *  - If this intent, do next steps as per config: ElicitIntent, Terminate, Transfer implemented for now
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

        if(!isRFCSet()){
            rfc=this.intent.getIntentName();
            request.getSessionAttributes().put("rfc",rfc);
            request.getSessionAttributes().put("rfcCodeHookAlias",intent.getIntentAlias());
            System.out.println(intent.getIntentName()+" RFC set: "+rfc);
        }
        String acknowledgementPrompt=acknowledgeIntent(request);
        LexResponse lexResponse= checkPrerequisites(request,context);
        if(lexResponse!=null){
            System.out.println(intent.getIntentName()+" Prerequisite response: "+lexResponse);
            return lexResponse;
        }

        lexResponse= fillMandatorySlots(acknowledgementPrompt,request);
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

    private LexResponse nextAction(LexEvent request) {
        LexResponse lexResponse= new LexResponse();
        Map<String,String> sessionAttributes= request.getSessionAttributes();
        if(!rfc.equals(intent.getIntentName())){
            request.getCurrentIntent().setName(rfc);

            final AmendBankDetailsCodeHook amendBankDetailsCodeHook = LambdaInvokerFactory.builder()
                    .lambdaClient(AWSLambdaClientBuilder.defaultClient()).functionAlias(rfcCodeHookAlias)
                    .build(AmendBankDetailsCodeHook.class);
            return amendBankDetailsCodeHook.invokeHook(request);
            /*
            AmazonLexRuntime client = AmazonLexRuntimeClientBuilder.defaultClient();
            com.amazonaws.services.lexruntime.model.DialogAction dialogAction = new com.amazonaws.services.lexruntime.model.DialogAction().withIntentName(rfc).withType(DialogActionType.ConfirmIntent).withMessage("DOES NT MATTER").withMessageFormat(MessageFormatType.PlainText);
            PutSessionRequest putSessionRequest= new PutSessionRequest().withBotName("ContactCenterBot").withBotAlias("$LATEST").withUserId(request.getUserId()).withSessionAttributes(request.getSessionAttributes()).withDialogAction(dialogAction);
            client.putSession(putSessionRequest);
            System.out.println("PutSession is successful: "+putSessionRequest);
            System.out.println("PutSession to original rfc:"+rfc);
            */


        }

        System.out.println("No next action for intent:"+intent.getIntentName());
        return finalRFCMessage();

    }

    protected abstract LexResponse finalRFCMessage();

    protected abstract LexResponse fulfillIntent();

    private LexResponse fillMandatorySlots(String acknowledgementPrompt,LexEvent request) {
        for(Slot mandatorySlot:intent.getMandatorySlots()) {
            String slotValue=request.getCurrentIntent().getSlots().get(mandatorySlot.getSlotName());
            System.out.println(intent.getIntentName()+": Slot value for "+mandatorySlot.getSlotName()+" is: "+slotValue);
            if(StringUtils.isNullOrEmpty(slotValue)){
                LexResponse lexResponse= new LexResponse();
                Map<String,String> sessionAttributes= request.getSessionAttributes();
                DialogAction dialogAction= new DialogAction();
                dialogAction.setIntentName(intent.getIntentName());
                dialogAction.setType("ElicitSlot");
                dialogAction.setSlotToElicit(mandatorySlot.getSlotName());
                Message message= new Message();
                message.setContentType("SSML");
                message.setContent((acknowledgementPrompt!=null)?mandatorySlot.getPrimaryPrompt():mandatorySlot.getPrimaryPrompt());
                dialogAction.setMessage(message);
                request.getSessionAttributes().put("intentAcknowledged","Yes");

                lexResponse.setSessionAttributes(sessionAttributes);
                lexResponse.setDialogAction(dialogAction);
                return lexResponse;
            }
        }
        return null;
    }

    protected LexResponse checkPrerequisites(LexEvent request, Context context){
        List<Prerequisite> preRequisites=intent.getPreRequisites();
        for(Prerequisite preRequisiteIntent:preRequisites){
            if(!intentsFulfilled.contains(preRequisiteIntent.getIntentName())){
                request.getCurrentIntent().setName(preRequisiteIntent.getIntentName());
                final IdentificationCodeHook identificationCodeHook = LambdaInvokerFactory.builder()
                        .lambdaClient(AWSLambdaClientBuilder.defaultClient()).functionAlias(preRequisiteIntent.getLambdaCodeHookAlias())
                        .build(IdentificationCodeHook.class);
                return identificationCodeHook.invokeHook(request);
            }
        }
        return null;
    }

    private String acknowledgeIntent(LexEvent request) {
        String acknowledgementPrompt=request.getSessionAttributes().get("acknowledgementPrompt");
        if(intent.isAcknowledgeIntent() && !intentAcknowledged ){
            request.getSessionAttributes().put("acknowledgementPrompt",intent.getAcknolwegementPrompt());
            return intent.getAcknolwegementPrompt();
        }
        if(!StringUtils.isNullOrEmpty(acknowledgementPrompt)&& !intentAcknowledged){
            return acknowledgementPrompt;
        }
        return null;
    }

    private boolean isRFCSet() {
        return !StringUtils.isNullOrEmpty(rfc);
    }

    private void initialize(LexEvent request) {
        setRFC(request);
        setIntentsFulfilled(request);
        setIntentAcknowledged(request);
        initializeIntent();
        initializeLambdaClient();


    }

    private void initializeLambdaClient() {

        lambdaClient= AWSLambdaClientBuilder.defaultClient();

    }

    protected abstract void initializeIntent();

    private void setIntentAcknowledged(LexEvent request) {
        String intentAcknowledgedString=request.getSessionAttributes().get("intentAcknowledged");
        String acknowledgeString=request.getSessionAttributes().get("acknowledgeString");
        if(!StringUtils.isNullOrEmpty(intentAcknowledgedString) && ("Yes".equals(intentAcknowledgedString))){
            intentAcknowledged=true;
            request.getSessionAttributes().put("intentAcknowledged","Yes");
        }
        intentAcknowledged=false;
    }

    private void setIntentsFulfilled(LexEvent request) {

        intentsFulfilled= new ArrayList<String>();
        String intentsString=request.getSessionAttributes().get("intentsFulfilled");
        if(intentsString!=null) {
            String[] splittedString = intentsString.split(",");
            for (String intentFulfilled : splittedString) {
                intentsFulfilled.add(intentFulfilled);
            }
        }
    }

    private void setRFC(LexEvent request) {
        this.rfc=request.getSessionAttributes().get("rfc");
        this.rfcCodeHookAlias=request.getSessionAttributes().get("rfcCodeHookAlias");
    }

}
