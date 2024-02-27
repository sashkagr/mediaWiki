package org.example.mediawiki.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mediawiki.modal.DescriptionGetApiResponse;
import org.example.mediawiki.modal.DescriptionGetApiSearchResponse;
import org.example.mediawiki.modal.Word;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class WikiApiRequest {
    private WikiApiRequest() {
    }

    private static final Logger logger = Logger.getLogger(WikiApiRequest.class.getName());

    public static List<Word> getDescriptionByTitle(String title) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Word> result = new ArrayList<>();
        String jsonResponse = "";
        try {
            String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=" + title + "&srwhat=text&format=json";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                jsonResponse = readResponse(conn);
                List<DescriptionGetApiSearchResponse> words = extractValidWords(jsonResponse, objectMapper);
                result = words.stream()
                        .map(WikiApiRequest::mapResponseToModel)
                        .collect(Collectors.toList());
            } else {
                if (responseCode > 0) {
                    logger.info("Error with response code");
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return result;
    }

    private static String readResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private static List<DescriptionGetApiSearchResponse> extractValidWords(String jsonResponse, ObjectMapper objectMapper) throws IOException {
        DescriptionGetApiResponse responses = objectMapper.readValue(jsonResponse, DescriptionGetApiResponse.class);
        List<DescriptionGetApiSearchResponse> words = new ArrayList<>();
        for (DescriptionGetApiSearchResponse searchResponse : responses.getQuery().getSearch()) {
            try {
                if (searchResponse.getDescription() != null && searchResponse.getTitle() != null && searchResponse.getId() != null) {
                    words.add(searchResponse);
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        return words;
    }

    private static Word mapResponseToModel(DescriptionGetApiSearchResponse descriptionGetApiSearchResponse) {
        Word word = new Word();
        word.setId(Integer.parseInt(descriptionGetApiSearchResponse.getId()));
        word.setTitle(descriptionGetApiSearchResponse.getTitle());
        word.setDescription(descriptionGetApiSearchResponse.getDescription());
        return word;
    }

    public static Word getDescriptionByPageId(int id) throws IOException {
        String id1 = Integer.toString(id);
        String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&pageids=" + id1;
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        JSONObject response = new JSONObject(content.toString());
        JSONObject pages = response.getJSONObject("query").getJSONObject("pages");
        JSONObject page = pages.getJSONObject(id1);
        String title = page.getString("title");
        String extract = page.getString("extract");

        int index = extract.indexOf("<h2>");
        if (index != -1) {
            extract = extract.substring(0, index);
        }

        extract = extract.replaceAll("<[^>]*>", "");
        extract = extract.replace("\n", "");
        Word word = new Word();
        word.setTitle(title);
        word.setDescription(extract.trim());
        return word;
    }
}
