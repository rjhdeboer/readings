package org.example.readings.statistics.web.controller;

import org.example.readings.statistics.domain.FetchReadingStatisticsQuery;
import org.example.readings.statistics.domain.ReadingStatistics;
import org.example.readings.statistics.domain.processor.QueryProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ReadingStatisticsQueryController {

    @Autowired
    private QueryProcessor<FetchReadingStatisticsQuery, ReadingStatistics> queryProcessor;

    @GetMapping(value = "/statistics")
    @PreAuthorize("hasRole('ROLE_ANALIST')")
    public ReadingStatistics fetchReadingStatistics(@RequestParam(required = false) String sensorId,
                                                    @RequestParam(required = false) String type,
                                                    @RequestParam(required = false) String category,
                                                    @RequestParam long from,
                                                    @RequestParam long until)
            throws MissingServletRequestParameterException {
        if (sensorId == null && type == null && category == null) {
            throw new MissingServletRequestParameterException("sensorId|type|category", "String");
        }

        return queryProcessor.handle(new FetchReadingStatisticsQuery(sensorId, type, category, from, until));
    }
}
