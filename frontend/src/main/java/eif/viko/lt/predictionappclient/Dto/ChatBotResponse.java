package eif.viko.lt.predictionappclient.Dto;

import java.util.List;

public class ChatBotResponse {

        private String bestCategory;
        private List<String> allCategories;
        public ChatBotResponse() {
        }
        public ChatBotResponse(String bestCategory, List<String> allCategories) {
            this.bestCategory = bestCategory;
            this.allCategories = allCategories;
        }
        public String getBestCategory() {
            return bestCategory;
        }
        public void setBestCategory(String bestCategory) {
            this.bestCategory = bestCategory;
        }
        public List<String> getAllCategories() {
            return allCategories;
        }
        public void setAllCategories(List<String> allCategories) {
            this.allCategories = allCategories;
        }
}
