package org.heyner.excelutils.application.commands.core;

import org.heyner.excelutils.application.commands.analyzetrx.AnalyzeTRXArgs;
import org.heyner.excelutils.application.commands.analyzetrx.AnalyzeTRXArgsMapper;
import org.heyner.excelutils.application.commands.directoryparser.DirectoryParserArgs;
import org.heyner.excelutils.application.commands.directoryparser.DirectoryParserArgsMapper;
import org.heyner.excelutils.application.commands.format_trx.FormatTRXArgs;
import org.heyner.excelutils.application.commands.format_trx.FormatTRXArgsMapper;
import org.heyner.excelutils.application.commands.formatactivity.FormatActivityArgs;
import org.heyner.excelutils.application.commands.formatactivity.FormatActivityArgsMapper;
import org.heyner.excelutils.application.commands.formatinvregisterln.FormatInvRegisterLNArgs;
import org.heyner.excelutils.application.commands.formatinvregisterln.FormatInvRegisterLNArgsMapper;
import org.heyner.excelutils.application.commands.fusiontrx.FusionTRXArgs;
import org.heyner.excelutils.application.commands.fusiontrx.FusionTRXArgsMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArgsMapperTest {

    @ParameterizedTest
    @MethodSource("provideMappers")
    void testMap(CommandArgsMapper mapper, String[] args, CommandArgs expected) {
        assertEquals(expected, mapper.map(args));
    }

    static Stream<Arguments> provideMappers() {
        return Stream.of(
            Arguments.of(new AnalyzeTRXArgsMapper(), new String[]{"analyzetrx", "input.xlsx"}, new AnalyzeTRXArgs(Path.of("input.xlsx"))),
            Arguments.of(new FormatTRXArgsMapper(), new String[]{"formattrx", "input.xlsx"}, new FormatTRXArgs(Path.of("input.xlsx"))),
            Arguments.of(new DirectoryParserArgsMapper(), new String[]{"directoryparser", "input.xlsx"}, new DirectoryParserArgs(Path.of("input.xlsx"))),
            Arguments.of(new FusionTRXArgsMapper(), new String[]{"fusiontrx", "dir", "output.xlsx"}, new FusionTRXArgs(Path.of("dir"), Path.of("output.xlsx"))),
            Arguments.of(new FormatActivityArgsMapper(), new String[]{"formatactivity", "input.xlsx"}, new FormatActivityArgs(Path.of("input.xlsx"))),
            Arguments.of(new FormatInvRegisterLNArgsMapper(), new String[]{"formatinvregisterln", "input.xlsx"}, new FormatInvRegisterLNArgs(Path.of("input.xlsx")))
        );
    }
}
