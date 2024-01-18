package software.ulpgc.moneycalculator.fixer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.ulpgc.moneycalculator.Currency;
import software.ulpgc.moneycalculator.ExchangeRate;
import software.ulpgc.moneycalculator.ExchangeRateLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;

public class FixerExchangeRateLoader implements ExchangeRateLoader {
    @Override
    public ExchangeRate load(Currency from, Currency to) {
        try {
            return ConversionPoints(from, to,loadJson(from,to));
        } catch (IOException e) {
            return new ExchangeRate(from, to, LocalDate.now(), 1);
        }
    }

    private ExchangeRate ConversionPoints(Currency from, Currency to, String json) {
        Map<String, JsonElement> rates = new Gson().fromJson(json, JsonObject.class).get("rates").getAsJsonObject().asMap();
        double fromRates = rates.get(from.code()).getAsDouble();
        double toRates = rates.get(to.code()).getAsDouble();
        return calculate(from,to,fromRates,toRates);
    }

    private ExchangeRate calculate(Currency from, Currency to, double fromRates, double toRates) {
        return new ExchangeRate(from, to, LocalDate.now(), getRate(fromRates, toRates));
    }

    private static double getRate(double fromRates, double toRates) {
        return (1 / fromRates) * toRates;
    }

    private String loadJson(Currency from, Currency to) throws IOException {
        URL url = new URL("http://data.fixer.io/api/" + LocalDate.now() +
                "?access_key=" + FixerAPI.key + "&symbols=" + from.code() +"," + to.code());
        try (InputStream is = url.openStream()) {
            return new String(is.readAllBytes());
        }
    }
}
