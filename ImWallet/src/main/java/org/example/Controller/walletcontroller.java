package org.example.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class walletcontroller {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndex(Model model) {
        List<SegmentInfo> segmentInfos = new ArrayList<>();
        List<JsonNode> fare = new ArrayList<>();


        try {
            String url = "https://partner.imwallet.in/web_services/statudentFilter.jsp";
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());

                JsonNode resultsNode = jsonNode.path("Response").path("Results");
                for (JsonNode result : resultsNode) {
                    for (JsonNode subResult : result) {
                        JsonNode segmentsNode = subResult.path("Segments");
                        for (JsonNode segment : segmentsNode) {
                            for (JsonNode subSegment : segment) {
                                JsonNode depTimeNode = subSegment.path("Origin").path("DepTime");
                                String depTime = depTimeNode.asText();
                                boolean stopOver = subSegment.path("StopOver").asBoolean();
                                String airlineName = subSegment.path("Airline").path("AirlineName").asText();
                                JsonNode fareNode = subSegment.path("Fare");
                                fare.add(fareNode);
                                double publishedFare = fareNode.path("PublishedFare").asDouble();
                                String timeCategory = getTimeCategory(depTime);

                                SegmentInfo segmentInfo = new SegmentInfo(timeCategory, depTime, stopOver, airlineName, publishedFare);
                                segmentInfos.add(segmentInfo);
                            }
                        }
                    }
                }


            } else {
                System.out.println("HTTP request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        for (JsonNode segmentInfo : fare) {
//            double publishedFare = segmentInfo.();
//            System.out.println("Published Fare: " + segmentInfo);
//        }

        model.addAttribute("segmentInfos", segmentInfos);
        return "index";
    }

    private String getTimeCategory(String depTime) {
        LocalTime time = LocalTime.parse(depTime.substring(11));
        if (time.isAfter(LocalTime.parse("00:00")) && time.isBefore(LocalTime.parse("12:00"))) {
            return "Morning";
        } else if (time.isAfter(LocalTime.parse("12:00")) && time.isBefore(LocalTime.parse("17:00"))) {
            return "Afternoon";
        } else if (time.isAfter(LocalTime.parse("17:00")) && time.isBefore(LocalTime.parse("20:00"))) {
            return "Evening";
        } else {
            return "Night";
        }
    }

    public class SegmentInfo {
        private String timeCategory;
        private String depTime;
        private boolean stopOver;
        private String airlineName;
        private double publishedFare;

        public SegmentInfo(String timeCategory, String depTime, boolean stopOver, String airlineName, double publishedFare) {
            this.timeCategory = timeCategory;
            this.depTime = depTime;
            this.stopOver = stopOver;
            this.airlineName = airlineName;
            this.publishedFare = publishedFare;
        }

        public String getTimeCategory() {
            return timeCategory;
        }

        public String getDepTime() {
            return depTime;
        }

        public boolean isStopOver() {
            return stopOver;
        }

        public String getAirlineName() {
            return airlineName;
        }

        public double getPublishedFare() {
            return publishedFare;
        }
    }
}
