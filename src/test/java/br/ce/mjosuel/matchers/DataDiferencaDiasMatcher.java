package br.ce.mjosuel.matchers;

import br.ce.mjosuel.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

    private Integer qtdDias;

    public DataDiferencaDiasMatcher(Integer qtdDias){
        this.qtdDias = qtdDias;
    }

    @Override
    public void describeTo(Description description) {

        Date dateEsperada = DataUtils.obterDataComDiferencaDias(qtdDias);
        DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
        description.appendText(format.format(dateEsperada));
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data,DataUtils.obterDataComDiferencaDias(qtdDias));
    }

}
