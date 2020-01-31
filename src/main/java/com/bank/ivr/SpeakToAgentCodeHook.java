package com.bank.ivr;

import com.bank.ivr.model.*;
import com.bank.ivr.model.Intent.IntentType;

import java.util.ArrayList;
import java.util.List;

public class SpeakToAgentCodeHook extends BaseHook {
	@Override
    protected LexResponse finalRFCMessage() {
	    //This will never get called

        DialogAction dialogAction= new DialogAction();
        dialogAction.setFulfillmentState("Fulfilled");
        dialogAction.setType("Close");
        Message message= new Message();

        message.setContent("Thanks. Transferring you to an agent who can help with this");
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
    protected void initializeIntent() {
    	Intent intent= new Intent();
        intent.setIntentName("query_speak_to_agent");
        intent.setAcknowledgeIntent(true);
        intent.setAcknolwegementPrompt("Okay. Speak to agent.");
        intent.setIntentType(IntentType.FURTHER_QUESTIONS);
        intent.setFurtherQuestion("What is it you would like to speak about? You can say things like amend bank details or my business name has changed");
        List<Slot> slots= new ArrayList<>();
        intent.setMandatorySlots(slots);
        intent.setPreRequisites(new ArrayList<>());
        intent.setIntentAlias("query_speak_to_agent");

        this.setIntent(intent);
    }

}
