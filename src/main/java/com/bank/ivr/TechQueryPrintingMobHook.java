package com.bank.ivr;

import java.util.ArrayList;
import java.util.List;

import com.bank.ivr.model.DialogAction;
import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Message;
import com.bank.ivr.model.Slot;
import com.bank.ivr.model.Intent.IntentType;

public class TechQueryPrintingMobHook extends BaseHook {
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
    protected void initializeIntentHook() {
	    /*
    	Intent intent= new Intent();
        intent.setIntentName("tech_query_printing_mobile");
        intent.setAcknowledgeIntent(true);
        intent.setAcknowledgementPrompt("Okay.");
        intent.setIntentType(IntentType.DISAMBIGUATION);
        List<Slot> slots= new ArrayList<>();
        intent.setMandatorySlots(slots);
        Slot machineID= new Slot();
        machineID.setSlotName("disamb_menu_conn_conntype");
        machineID.setPrimaryPrompt("What type of connection do you have?");
        String[] noMatchPrompts = new String[2];
        noMatchPrompts[0] = "What type of connection do you have?";
        noMatchPrompts[1] = "What type of connection do you have?";
        machineID.setNoMatchPrompts(noMatchPrompts);
        slots.add(machineID);
        intent.setMandatorySlots(slots);
        intent.setPreRequisites(new ArrayList<>());
<<<<<<< HEAD
        intent.setIntentFunction("tech_query_printing_mobile");

=======
        intent.setIntentAlias("tech_query_printing_mobile");
        intent.setDefaultTag("menu_not_sure_query");
        
        List<Slot> optionalSlots = new ArrayList<>();
        intent.setOptionalSlots(optionalSlots);
        
>>>>>>> origin/feature/lexcodehook
        this.setIntent(intent);
        */
    }

}
