package org.heyner.excelutils.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void init() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("org.heyner.excelutils");
    }

    @Test
    void application_ne_depend_pas_de_infrastructure() {

        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..application..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    void pas_de_cycles_entre_packages_metier() {

        ArchRule rule = SlicesRuleDefinition.slices()
                .matching("org.heyner.excelutils.(*)..")
                .should()
                .beFreeOfCycles();

        rule.check(importedClasses);
    }

    @Test
    void infrastructure_ne_depend_pas_de_bootstrap() {

        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..infrastructure..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..bootstrap..");

        rule.check(importedClasses);
    }

    @Test
    void shared_ne_depend_pas_de_application() {

        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..shared..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..application..");

        rule.check(importedClasses);
    }

    @Test
    void shared_ne_depend_pas_de_infrastructure() {

        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..shared..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }
}