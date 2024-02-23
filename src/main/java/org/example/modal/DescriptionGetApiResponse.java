package org.example.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionGetApiResponse {

        @JsonProperty("query")
        private DescriptionGetApiQueryResponse query;

        public DescriptionGetApiQueryResponse getQuery() {
            return query;
        }

        public void setQuery(DescriptionGetApiQueryResponse query) {

            this.query = query;
        }




}
