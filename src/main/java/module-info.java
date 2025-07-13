module ru.itis.vhsroni.semestrovka {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    exports ru.itis.vhsroni.semestrovka.protocol to com.fasterxml.jackson.databind;


    opens ru.itis.vhsroni.semestrovka to javafx.fxml;
    exports ru.itis.vhsroni.semestrovka;

    opens ru.itis.vhsroni.semestrovka.controller to javafx.fxml;
    exports ru.itis.vhsroni.semestrovka.controller;
    exports ru.itis.vhsroni.semestrovka.controller.impl;
    opens ru.itis.vhsroni.semestrovka.controller.impl to javafx.fxml;
    exports ru.itis.vhsroni.semestrovka.listener.impl;
    opens ru.itis.vhsroni.semestrovka.listener.impl to javafx.fxml;
    exports ru.itis.vhsroni.semestrovka.game.logic;
    opens ru.itis.vhsroni.semestrovka.game.logic to javafx.fxml;
}