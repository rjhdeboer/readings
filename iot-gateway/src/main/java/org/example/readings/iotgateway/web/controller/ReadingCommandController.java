package org.example.readings.iotgateway.web.controller;

import org.example.readings.iotgateway.domain.AddReadingCommand;
import org.example.readings.iotgateway.domain.processor.CommandProcessor;
import org.example.readings.iotgateway.web.dto.ReadingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ReadingCommandController {

    private static final Logger logger = LoggerFactory.getLogger(ReadingCommandController.class);

    @Autowired
    private CommandProcessor<AddReadingCommand> commandProcessor;

    @PostMapping(value = "/readings")
    public void addReading(@RequestBody @Valid ReadingDto reading) {
        logger.info("Received reading: " + reading);
        commandProcessor.process(new AddReadingCommand(reading.getType(), reading.getCategory(), reading.getValue(),
                reading.getTakenAt()));
    }
}
