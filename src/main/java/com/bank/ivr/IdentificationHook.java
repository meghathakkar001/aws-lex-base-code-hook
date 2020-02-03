package com.bank.ivr;

import com.bank.ivr.model.DialogAction;
import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Message;
import com.bank.ivr.model.Slot;
import com.bank.ivr.model.Intent.IntentType;

import java.util.ArrayList;
import java.util.List;

public class IdentificationHook extends BaseHook{
    @Override
    protected LexResponse finalRFCMessage() {
    	DialogAction dialogAction= new DialogAction();
        dialogAction.setFulfillmentState("Fulfilled");
        dialogAction.setType("Close");
        Message message= new Message();

        message.setContent("<speak>Let me put you through to someone who can help</speak>");
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
