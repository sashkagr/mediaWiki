package org.example.mediawiki.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionGetApiQueryResponse {

    @JsonProperty("search")
    private List<DescriptionGetApiSearchResponse> search;

    public List<DescriptionGetApiSearchResponse> getSearch() {
        return search;
    }

    public void setSearch(List<DescriptionGetApiSearchResponse> search) {

        this.search = search;
    }

}
