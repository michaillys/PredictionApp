module eif.viko.lt.predictionappclient {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.prefs;
    requires java.sql;
    requires com.google.gson;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires okhttp3;
    requires java.net.http;


    opens eif.viko.lt.predictionappclient.Dto to com.google.gson;

    opens eif.viko.lt.predictionappclient to javafx.fxml;
    exports eif.viko.lt.predictionappclient;
}