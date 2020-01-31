package com.bank.ivr;

import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Slot;
import com.bank.ivr.model.Intent.IntentType;

import java.util.ArrayList;
import java.util.List;

public class IdentificationHook extends BaseHook{
    @Override
    protected LexResponse finalRFCMessage() {
        return null;
        //identification will never get a final message request
    }

    @Override
    protected LexResponse fulfillIntent() {
        return null;
    }

    @Override
    protected void initializeIntentHook() {

    }


}
