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

}
