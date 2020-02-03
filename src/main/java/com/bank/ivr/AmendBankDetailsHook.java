package com.bank.ivr;

import com.bank.ivr.model.*;
import com.bank.ivr.model.Intent.IntentType;

import java.util.ArrayList;
import java.util.List;

public class AmendBankDetailsHook extends BaseHook{
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
    
    
}
