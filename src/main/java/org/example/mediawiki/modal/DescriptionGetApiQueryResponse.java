package org.example.mediawiki.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionGetApiQueryResponse {

    @JsonProperty("search")
    private List<DescriptionGetApiSearchResponse> search;


}
