package com.bank.ivr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.events.LexEvent;
import com.amazonaws.util.StringUtils;
import com.bank.ivr.model.DialogAction;
import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Message;
import com.bank.ivr.model.Slot;
import com.bank.ivr.model.Intent.IntentType;

public class TechQueryConnBroadbandHook extends BaseHook {
	@Override
    protected LexResponse finalRFCMessage() {
        DialogAction dialogAction= new DialogAction();
        dialogAction.setFulfillmentState("Fulfilled");
        dialogAction.setType("Close");
        Message message= new Message();

        message.setContent("<speak>Thanks. Transferring you to an agent who can help with this</speak>");
        message.setContentType("SSML");
        dialogAction.setMessage(message);
        //dialogAction.setSlots(intent.get);
        LexResponse lexResponse= new LexResponse();
        lexResponse.setDialogAction(dialogAction);

        return lexResponse;
    }

    @Override
    protected LexResponse fulfillIntent() {
        return null;
    }
    
    
    @Override
	protected LexResponse fillOptionalSlots(LexEvent request) {
		for (Slot optionalSlot : getIntent().getOptionalSlots()) {
			if (request.getSessionAttributes().get(
					optionalSlot.getSlotName() + "Count") == null) {
				request.getSessionAttributes().put(
						optionalSlot.getSlotName() + "Count", "0");
			} else {
				int count = Integer.parseInt(request.getSessionAttributes()
						.get(optionalSlot.getSlotName() + "Count"));
				request.getSessionAttributes().put(
						optionalSlot.getSlotName() + "Count",
						String.valueOf(++count));
				System.out.println("NM/NI count " + count);
			}
			String slotValue = request.getCurrentIntent().getSlots()
					.get(optionalSlot.getSlotName());
			System.out.println(getIntent().getIntentName()
					+ ": Slot value for " + optionalSlot.getSlotName()
					+ " is: " + slotValue);
			if (StringUtils.isNullOrEmpty(slotValue)) {
				String slotPrompt = getSlotPrompt(request, optionalSlot);
				LexResponse lexResponse = new LexResponse();
				if (!StringUtils.isNullOrEmpty(slotPrompt)) {

					Map<String, String> sessionAttributes = request
							.getSessionAttributes();
					DialogAction dialogAction = new DialogAction();
					dialogAction.setIntentName(getIntent().getIntentName());
					dialogAction.setType("ElicitSlot");
					dialogAction.setSlotToElicit(optionalSlot.getSlotName());
					if (this.getIntent().getIntentName()
							.equals(request.getCurrentIntent().getName())) {
						dialogAction.setSlots(request.getCurrentIntent()
								.getSlots());
					}
					Message message = new Message();
					message.setContentType("SSML");

					message.setContent(slotPrompt);
					dialogAction.setMessage(message);

					lexResponse.setSessionAttributes(sessionAttributes);
					lexResponse.setDialogAction(dialogAction);
					return lexResponse;
				}
			}

			else {
				request.getSessionAttributes().remove(
						optionalSlot.getSlotName() + "Count");
				if (slotValue.equalsIgnoreCase("no")) {
					System.out.println("Slot value is 'no'");
					continue;
				} else {
					System.out.println("Slot value is 'yes'");
					return hangUp();
				}
			}
		}

		return null;
	}

	private LexResponse hangUp() {
		DialogAction dialogAction = new DialogAction();
		dialogAction.setFulfillmentState("Fulfilled");
		dialogAction.setType("Close");
		Message message = new Message();

		message.setContent("<speak>Thanks for calling bank. Goodbye</speak>");
		message.setContentType("SSML");
		dialogAction.setMessage(message);
		// dialogAction.setSlots(intent.get);
		LexResponse lexResponse = new LexResponse();
		lexResponse.setDialogAction(dialogAction);

		return lexResponse;
	}


}
