package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.modal.DescriptionGetApiResponse;
import org.example.modal.DescriptionGetApiSearchResponse;
import org.example.modal.Word;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WikiApiRequest {
    public static List<Word> getDescriptionByTitle(String title) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Word> result = new ArrayList<>();
        String jsonResponse = "";
        try {
            String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="+title+"&srwhat=text&format=json";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response here and extract the book information
                jsonResponse = response.toString();
                DescriptionGetApiResponse responses = objectMapper.readValue(jsonResponse, DescriptionGetApiResponse.class);
                List<DescriptionGetApiSearchResponse> words = new ArrayList<>();
                for (DescriptionGetApiSearchResponse searchResponse : responses.getQuery().getSearch()) {
                    try {

                        if (searchResponse.getDescription() != null && searchResponse.getTitle() != null) {
                            words.add(searchResponse);
                        }
                    } catch (Exception e) {
                    }
                }
                result = words.stream()
                        .map(WikiApiRequest::mapResponseToModel)
                        .collect(Collectors.toList());
            }
            else {
                System.out.println("Error: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Word mapResponseToModel(DescriptionGetApiSearchResponse descriptionGetApiSearchResponse) {
        Word word = new Word();
        word.setTitle(descriptionGetApiSearchResponse.getTitle());
        word.setDescription(descriptionGetApiSearchResponse.getDescription());
        return word;
    }
}
