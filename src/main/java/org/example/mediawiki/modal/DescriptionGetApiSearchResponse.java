package org.example.mediawiki.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionGetApiSearchResponse {

    @JsonProperty("pageid")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("snippet")
    private String description;


}
