package org.app.adapter.primary.rest;

import org.app.adapter.primary.rest.api.CurrencyApi;
import org.app.adapter.primary.rest.model.*;
import org.app.domain.Currency;
import org.app.domain.Recommendation;
import org.app.usecase.*;
import org.app.usecase.request.CurrencyHistoryRequestModel;
import org.app.usecase.request.CurrencyRecommendationRequestModel;
import org.app.usecase.request.RecommendedCurrencyRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class CurrencyController implements CurrencyApi {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyController.class);

    private CreateCurrencyHistoryRequestUseCase createCurrencyHistoryRequestUseCase;
    private GetCurrencyHistoryUseCase getCurrencyHistoryUseCase;

    private CreateRecommendedCurrencyUseCase createRecommendedCurrencyUseCase;
    private GetRecommendedCurrencyUseCase getRecommendedCurrencyUseCase;

    private AcceptCurrencyRecommendationUseCase acceptCurrencyRecommendationUseCase;
    private GetAllCurrenciesUseCase getAllCurrenciesUseCase;

    public CurrencyController(CreateCurrencyHistoryRequestUseCase createCurrencyHistoryRequestUseCase,
                              GetCurrencyHistoryUseCase getCurrencyHistoryUseCase,
                              CreateRecommendedCurrencyUseCase createRecommendedCurrencyUseCase,
                              GetRecommendedCurrencyUseCase getRecommendedCurrencyUseCase,
                              AcceptCurrencyRecommendationUseCase acceptCurrencyRecommendationUseCase,
                              GetAllCurrenciesUseCase getAllCurrenciesUseCase) {

        this.createCurrencyHistoryRequestUseCase = createCurrencyHistoryRequestUseCase;
        this.getCurrencyHistoryUseCase = getCurrencyHistoryUseCase;
        this.createRecommendedCurrencyUseCase = createRecommendedCurrencyUseCase;
        this.getRecommendedCurrencyUseCase = getRecommendedCurrencyUseCase;
        this.acceptCurrencyRecommendationUseCase = acceptCurrencyRecommendationUseCase;
        this.getAllCurrenciesUseCase = getAllCurrenciesUseCase;
    }

    @Override
    public ResponseEntity<RequestResponse> createCurrencyHistoryRequest(@PathVariable("currencySymbol") String currencySymbol,
                                                                        @PathVariable("currencyTime") Long currencyTime) {

        LOG.info("create history request");
        String requestId = UUID.randomUUID().toString();
        RequestResponse requestResponse = new RequestResponse()
                .requestId(requestId)
                .time(String.valueOf(LocalDateTime.now()));

        CurrencyHistoryRequestModel requestModel = new CurrencyHistoryRequestModel(requestId, currencySymbol,
                currencyTime, null);
        //TODO provide email
        createCurrencyHistoryRequestUseCase.execute(requestModel);
        LOG.info("created history request with id {}", requestId);
        return ResponseEntity.ok(requestResponse);
    }

    @Override
    public ResponseEntity<List<CurrencyDto>> getCurrencyHistory(@PathVariable("historyId") String historyId) {

        Collection<Currency> currencies = getCurrencyHistoryUseCase.execute(historyId);
        List<CurrencyDto> collect = currencies.stream().map(CurrencyDtoConverter::convert).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    @Override
    public ResponseEntity<RequestResponse> createRecommendedRequest(@Valid @RequestBody RecommendedSubmit recommendedSubmit) {

        LOG.info("create recommended request");
        String requestId = UUID.randomUUID().toString();
        RequestResponse requestResponse = new RequestResponse()
                .requestId(requestId)
                .time(String.valueOf(LocalDateTime.now()));

        //TODO Email null
        RecommendedCurrencyRequestModel requestModel = new RecommendedCurrencyRequestModel(requestId,
                recommendedSubmit.getCurrency(),
                recommendedSubmit.getValue(),
                recommendedSubmit.getTime().longValue(),
                null);

        createRecommendedCurrencyUseCase.execute(requestModel);
        LOG.info("created recommended request with id {}", requestId);
        return ResponseEntity.ok(requestResponse);
    }

    @Override
    public ResponseEntity<RecommendationDTO> getRecommended(@PathVariable("recommendedId") String recommendedId) {

        Recommendation recommendation = getRecommendedCurrencyUseCase.execute(recommendedId);
        return ResponseEntity.ok(RecommendationDtoConverter.convert(recommendation));
    }

    @Override
    public ResponseEntity<RequestResponse> acceptCurrencyOpinion(@Valid @RequestBody CurrencyOpinionSubmit currencyOpinionSubmit) {

        CurrencyRecommendationRequestModel requestModel = new CurrencyRecommendationRequestModel(currencyOpinionSubmit.getRequestId(),
                currencyOpinionSubmit.getScore(),
                currencyOpinionSubmit.getOpinion());

        acceptCurrencyRecommendationUseCase.execute(requestModel);

        RequestResponse requestResponse = new RequestResponse()
                .requestId(requestModel.getRequestId())
                .time(String.valueOf(LocalDateTime.now()));

        return ResponseEntity.ok(requestResponse);
    }

    @Override
    public ResponseEntity<List<CurrencyEntry>> getAllCurrencies() {
        List<String> execute = getAllCurrenciesUseCase.execute();

        List<CurrencyEntry> currencyEntryList = execute.stream().map(c -> {
            CurrencyEntry entry = new CurrencyEntry();
            entry.setSymbol(c);
            return entry;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(currencyEntryList);
    }
}
