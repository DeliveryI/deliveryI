package com.sparta.deliveryi;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.sparta.deliveryi", importOptions = ImportOption.DoNotIncludeTests.class)
class LayeredArchitectureTest {

    private static final String[] DOMAIN_PACKAGES = {
            "com.sparta.deliveryi.ai.domain..",
            "com.sparta.deliveryi.menu.domain..",
            "com.sparta.deliveryi.store.domain..",
            "com.sparta.deliveryi.user.domain.."
    };

    @ArchTest
    public void layeredArchitecture(JavaClasses classes) {
        Architectures.layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                .layer("Presentation").definedBy("com.sparta.deliveryi..presentation..")
                .layer("Application").definedBy("com.sparta.deliveryi..application..")
                .layer("Domain").definedBy("com.sparta.deliveryi..domain..")
                .layer("Infrastructure").definedBy("com.sparta.deliveryi..infrastructure..")
                // Presentation은 하위 계층(Application, Domain, Infrastructure)만 접근 가능
                .whereLayer("Presentation").mayOnlyAccessLayers("Application", "Domain", "Infrastructure")
                // Application은 Domain, Infrastructure만 접근 가능
                .whereLayer("Application").mayOnlyAccessLayers("Domain", "Infrastructure")
                // Domain은 Infrastructure만 접근 가능
                .whereLayer("Domain").mayOnlyAccessLayers("Infrastructure")
                // Infrastructure는 어떤 레이어에서도 접근할 수 없음
                .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
                // global 패키지는 모든 계층에서 자유롭게 접근 가능하도록 제외
                .ignoreDependency(
                        DescribedPredicate.alwaysTrue(),
                        JavaClass.Predicates.resideInAPackage("com.sparta.deliveryi.global..")
                )
                .check(classes);
    }

    @ArchTest
    public void domainsShouldNotDependOnEachOther(JavaClasses classes) {
        for (String domainPackage : DOMAIN_PACKAGES) {
            String[] otherDomains = Arrays.stream(DOMAIN_PACKAGES)
                    .filter(pkg -> !pkg.equals(domainPackage))
                    .toArray(String[]::new);

            noClasses()
                    .that().resideInAPackage(domainPackage)
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(otherDomains)
                    .because("도메인 간에는 서로 의존할 수 없습니다 (global 제외)")
                    .check(classes);
        }
    }

    @ArchTest
    public void globalShouldNotDependOnDomains(JavaClasses classes) {
        noClasses()
                .that().resideInAPackage("com.sparta.deliveryi.global..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(DOMAIN_PACKAGES)
                .because("global 패키지는 어떤 도메인에도 의존해서는 안됩니다")
                .check(classes);
    }
}
