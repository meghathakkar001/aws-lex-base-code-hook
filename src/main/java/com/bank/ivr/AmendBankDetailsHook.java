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

    @Override
    protected void initializeIntent() {
        Intent intent= new Intent();
        intent.setIntentName("amend_bank_details");
        intent.setAcknowledgeIntent(true);
        intent.setMandatorySlots(new ArrayList<Slot>());
        intent.setAcknolwegementPrompt("Okay. Amend Bank Details.");
        intent.setIntentType(IntentType.DEFAULT);
        List<Prerequisite> preRequisites= new ArrayList<>();
        Prerequisite prerequisite= new Prerequisite();
        prerequisite.setIntentName("identification");
        prerequisite.setLambdaCodeHookAlias("IdentificationCodeHook");
        preRequisites.add(prerequisite);
        intent.setPreRequisites(preRequisites);
        intent.setIntentAlias("AmendBankDetails");

        this.setIntent(intent);
    }
    
}
