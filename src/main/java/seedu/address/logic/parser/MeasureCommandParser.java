package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEASUREMENT_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEASUREMENT_VALUE;

import java.math.BigDecimal;
import java.time.LocalDate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MeasureCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Measurement;
import seedu.address.model.person.MeasurementType;

/**
 * Parses the displayed client index and required measurement arguments.
 */
public class MeasureCommandParser implements Parser<MeasureCommand> {
    public static final String MESSAGE_NON_NUMERIC = "Measurement value must be numeric.";

    @Override
    public MeasureCommand parse(String args) throws ParseException {
        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args,
                PREFIX_DATE, PREFIX_MEASUREMENT_TYPE, PREFIX_MEASUREMENT_VALUE);
        for (Prefix prefix : new Prefix[] {PREFIX_DATE, PREFIX_MEASUREMENT_TYPE, PREFIX_MEASUREMENT_VALUE}) {
            if (arguments.getAllValues(prefix).size() != 1) {
                throw new ParseException(MeasureCommand.MESSAGE_INVALID_FORMAT);
            }
        }
        try {
            Index index = ParserUtil.parseClientIndex(arguments.getPreamble());
            LocalDate date = ParserUtil.parseRecordDate(arguments.getValue(PREFIX_DATE).orElseThrow(),
                    Measurement.MESSAGE_FUTURE_DATE);
            MeasurementType type = ParserUtil.parseMeasurementType(
                    arguments.getValue(PREFIX_MEASUREMENT_TYPE).orElseThrow());
            String value = arguments.getValue(PREFIX_MEASUREMENT_VALUE).orElseThrow().trim();
            if (!value.matches("[+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)")) {
                throw new ParseException(MESSAGE_NON_NUMERIC);
            }
            return new MeasureCommand(index, new Measurement(date, type, new BigDecimal(value)));
        } catch (ParseException e) {
            throw new ParseException(e.getMessage() + "\nUse: " + MeasureCommand.MESSAGE_USAGE, e);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage() + "\nUse: " + MeasureCommand.MESSAGE_USAGE, e);
        }
    }
}
