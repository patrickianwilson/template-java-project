package com.patrickwilsonconsulting.resource.exceptions;

import com.patrickwilsonconsulting.serializers.ApiObject;
import lombok.Builder;
import lombok.Data;

/**
 * Created by pwilson on 9/21/17.
 */
@Data
@Builder
public class ExceptionModel implements ApiObject {
    private String code;
    private String reason;
    private Object details;
}
