package com.bank.ivr.model;

import java.util.List;

public class Intent {

    private String intentName;
    private String intentFunction;
    private List<Prerequisite> preRequisites;
    private IntentType intentType;
    private String defaultTag;
	private boolean acknowledgeIntent;
    private String acknowledgementPrompt;
    private List<Slot> mandatorySlots;
    private List<Slot> optionalSlots;
    private String furtherQuestion;

    public String getFurtherQuestion() {
        return furtherQuestion;
    }

    public void setFurtherQuestion(String furtherQuestion) {
        this.furtherQuestion = furtherQuestion;
    }

    public List<Prerequisite> getPreRequisites() {
        return preRequisites;
    }

    public void setPreRequisites(List<Prerequisite> preRequisites) {
        this.preRequisites = preRequisites;
    }

    public IntentType getIntentType() {
        return intentType;
    }
    
    public String getDefaultTag() {
		return defaultTag;
	}

	public void setDefaultTag(String defaultTag) {
		this.defaultTag = defaultTag;
	}

    public void setIntentType(IntentType intentType) {
        this.intentType = intentType;
    }

    public boolean isAcknowledgeIntent() {
        return acknowledgeIntent;
    }

    public String getIntentFunction() {
        return intentFunction;
    }

    public void setIntentFunction(String intentFunction) {
        this.intentFunction = intentFunction;
    }

    public void setAcknowledgeIntent(boolean acknowledgeIntent) {
        this.acknowledgeIntent = acknowledgeIntent;
    }

    public String getAcknowledgementPrompt() {
        return acknowledgementPrompt;
    }

    public void setAcknowledgementPrompt(String acknowledgementPrompt) {
        this.acknowledgementPrompt = acknowledgementPrompt;
    }

    public List<Slot> getMandatorySlots() {
        return mandatorySlots;
    }

    public void setMandatorySlots(List<Slot> mandatorySlots) {
        this.mandatorySlots = mandatorySlots;
    }

    public List<Slot> getOptionalSlots() {
        return optionalSlots;
    }

    public void setOptionalSlots(List<Slot> optionalSlots) {
        this.optionalSlots = optionalSlots;
    }


    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    @Override
    public String toString() {
        return "Intent{" +
                "intentName='" + intentName + '\'' +
                ", intentFunction='" + intentFunction + '\'' +
                ", preRequisites=" + preRequisites +
                ", intentType=" + intentType +
                ", acknowledgeIntent=" + acknowledgeIntent +
                ", acknowledgementPrompt='" + acknowledgementPrompt + '\'' +
                ", mandatorySlots=" + mandatorySlots +
                ", optionalSlots=" + optionalSlots +
                ", furtherQuestion='" + furtherQuestion + '\'' +
                '}';
    }


    public enum IntentType {
        DEFAULT,DISAMBIGUATION,FURTHER_QUESTIONS
    }
    enum FulfillmentType {
        AGENT,LEX
    }

    public static final class IntentBuilder {
        private String intentName;
        private String intentFunction;
        private List<Prerequisite> preRequisites;
        private IntentType intentType;
        private boolean acknowledgeIntent;
        private String acknolwegementPrompt;
        private List<Slot> mandatorySlots;
        private List<Slot> optionalSlots;

        private IntentBuilder() {
        }

        public static IntentBuilder anIntent() {
            return new IntentBuilder();
        }

        public IntentBuilder withIntentName(String intentName) {
            this.intentName = intentName;
            return this;
        }

        public IntentBuilder withIntentAlias(String intentAlias) {
            this.intentFunction = intentAlias;
            return this;
        }

        public IntentBuilder withPreRequisites(List<Prerequisite> preRequisites) {
            this.preRequisites = preRequisites;
            return this;
        }

        public IntentBuilder withIntentType(IntentType intentType) {
            this.intentType = intentType;
            return this;
        }

        public IntentBuilder withAcknowledgeIntent(boolean acknowledgeIntent) {
            this.acknowledgeIntent = acknowledgeIntent;
            return this;
        }

        public IntentBuilder withAcknolwegementPrompt(String acknolwegementPrompt) {
            this.acknolwegementPrompt = acknolwegementPrompt;
            return this;
        }

        public IntentBuilder withMandatorySlots(List<Slot> mandatorySlots) {
            this.mandatorySlots = mandatorySlots;
            return this;
        }

        public IntentBuilder withOptionalSlots(List<Slot> optionalSlots) {
            this.optionalSlots = optionalSlots;
            return this;
        }

        public Intent build() {
            Intent intent = new Intent();
            intent.setIntentName(intentName);
            intent.setPreRequisites(preRequisites);
            intent.setIntentType(intentType);
            intent.setAcknowledgeIntent(acknowledgeIntent);
            intent.setAcknowledgementPrompt(acknolwegementPrompt);
            intent.setMandatorySlots(mandatorySlots);
            intent.setOptionalSlots(optionalSlots);
            intent.intentFunction = this.intentFunction;
            return intent;
        }
    }
}
