package com.neotys.advanced.action.apache.kafka.send;

import com.neotys.action.argument.ArgumentValidator;
import com.neotys.action.argument.DefaultArgumentValidator;
import com.neotys.action.argument.Option;
import com.neotys.extensions.action.ActionParameter;

enum KafkaSendOption implements com.neotys.action.argument.Option {
    ConnectionName("connectionName", Option.OptionalRequired.Required, Option.AppearsByDefault.True, ActionParameter.Type.TEXT, "myConnection", "The name of the connection to map with other advanced actions.", DefaultArgumentValidator.NON_EMPTY),
    Topic("topic", Option.OptionalRequired.Required, Option.AppearsByDefault.True, ActionParameter.Type.TEXT, "", "The topic the record will be appended to.", DefaultArgumentValidator.NON_EMPTY),
    Key("key", Option.OptionalRequired.Optional, Option.AppearsByDefault.False, ActionParameter.Type.TEXT, "", "The key that will be included in the record.", DefaultArgumentValidator.NON_EMPTY),
    Message("message", Option.OptionalRequired.Required, Option.AppearsByDefault.True, ActionParameter.Type.TEXT, "", "The record contents", DefaultArgumentValidator.NON_EMPTY);

    private final String name;
    private final Option.OptionalRequired optionalRequired;
    private final Option.AppearsByDefault appearsByDefault;
    private final ActionParameter.Type type;
    private final String defaultValue;
    private final String description;
    private final ArgumentValidator argumentValidator;

    private KafkaSendOption(String name, Option.OptionalRequired optionalRequired, Option.AppearsByDefault appearsByDefault, ActionParameter.Type type, String defaultValue, String description, ArgumentValidator argumentValidator) {
        this.name = name;
        this.optionalRequired = optionalRequired;
        this.appearsByDefault = appearsByDefault;
        this.type = type;
        this.defaultValue = defaultValue;
        this.description = description;
        this.argumentValidator = argumentValidator;
    }

    public String getName() {
        return this.name;
    }

    public Option.OptionalRequired getOptionalRequired() {
        return this.optionalRequired;
    }

    public Option.AppearsByDefault getAppearsByDefault() {
        return this.appearsByDefault;
    }

    public ActionParameter.Type getType() {
        return this.type;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String getDescription() {
        return this.description;
    }

    public ArgumentValidator getArgumentValidator() {
        return this.argumentValidator;
    }
}
