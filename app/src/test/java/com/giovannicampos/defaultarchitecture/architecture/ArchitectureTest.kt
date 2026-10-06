package com.giovannicampos.defaultarchitecture.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.withAnnotationNamed
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

private const val ROOT = "com.giovannicampos.defaultarchitecture"

class LayerTest {

    @Test
    fun `given production code, when checking layers, then presentation and data depend only on domain`() {
        Konsist.scopeFromProduction().assertArchitecture {
            val domain = Layer("Domain", "$ROOT..domain..")
            val data = Layer("Data", "$ROOT..data..")
            val presentation = Layer("Presentation", "$ROOT..presentation..")

            domain.dependsOnNothing()
            data.dependsOn(domain)
            presentation.dependsOn(domain)
        }
    }

    @Test
    fun `given domain files, when checking imports, then none import Android, infra or DI frameworks`() {
        val forbidden = listOf("android.", "androidx.", "retrofit2.", "okhttp3.", "kotlinx.serialization.", "dagger.", "javax.inject.")
        Konsist.scopeFromProduction().files
            .filter { it.packagee?.name?.contains(".domain") == true }
            .assertFalse(additionalMessage = "Domain é Kotlin puro e sem @Inject: mova o código para data/presentation e crie o use case no <Feature>DomainModule.") { file ->
                file.imports.any { imp -> forbidden.any { imp.name.startsWith(it) } }
            }
    }

    @Test
    fun `given feature packages, when checking imports, then no feature imports another feature`() {
        Konsist.scopeFromProduction().files
            .filter { it.packagee?.name?.startsWith("$ROOT.") == true && it.packagee?.name?.startsWith("$ROOT.core") == false }
            .assertFalse(additionalMessage = "Features não se conhecem: comunique por navegação no MainActivity ou por contrato em core.") { file ->
                val own = file.packagee?.name?.removePrefix("$ROOT.")?.substringBefore('.')
                file.imports.any { imp ->
                    // "" = classe no pacote base (MainActivity, R): não é feature
                    val segment = imp.name.removePrefix("$ROOT.").substringBefore('.', missingDelimiterValue = "")
                    imp.name.startsWith("$ROOT.") && segment.isNotEmpty() && segment != own && segment != "core"
                }
            }
    }
}

class PackageTest {

    private fun assertSuffixInPackage(suffix: String, pkg: String) {
        Konsist.scopeFromProduction().classesAndInterfacesAndObjects()
            .filter { it.name.endsWith(suffix) }
            .assertTrue(additionalMessage = "Classes *$suffix ficam em <feature>/$pkg (skill android-architecture).") {
                it.packagee?.name?.endsWith(".$pkg") == true
            }
    }

    @Test
    fun `given UiState classes, when checking package, then they live in presentation ui state`() =
        assertSuffixInPackage("UiState", "presentation.ui.state")

    @Test
    fun `given UiIntent classes, when checking package, then they live in presentation ui intent`() =
        assertSuffixInPackage("UiIntent", "presentation.ui.intent")

    @Test
    fun `given UiEffect classes, when checking package, then they live in presentation ui effect`() =
        assertSuffixInPackage("UiEffect", "presentation.ui.effect")

    @Test
    fun `given ViewModels, when checking package, then they live in presentation viewmodel`() =
        assertSuffixInPackage("ViewModel", "presentation.viewmodel")

    @Test
    fun `given use cases, when checking package, then they live in domain usecase`() =
        assertSuffixInPackage("UseCase", "domain.usecase")

    @Test
    fun `given repository implementations, when checking package, then they live in data repository`() =
        assertSuffixInPackage("RepositoryImpl", "data.repository")
}

class MviTest {

    @Test
    fun `given ViewModels, when checking constructor, then none receive Context or repositories`() {
        Konsist.scopeFromProduction().classes()
            .withNameEndingWith("ViewModel")
            .assertFalse(additionalMessage = "ViewModel depende só de use cases e não recebe Context; mensagens viram UiEffect resolvido na Route.") { vm ->
                vm.primaryConstructor?.parameters?.any {
                    it.type.name == "Context" || it.type.name.endsWith("Repository") || it.type.name.endsWith("DataSource")
                } == true
            }
    }

    @Test
    fun `given Screen composables, when checking parameters, then they take uiState and onIntent but no ViewModel`() {
        Konsist.scopeFromProduction().functions()
            .withAnnotationNamed("Composable")
            .withNameEndingWith("Screen")
            .assertFalse(additionalMessage = "Screen é stateless: receba (uiState, onIntent). Só a Route conhece o ViewModel.") { fn ->
                fn.parameters.any { it.type.name.endsWith("ViewModel") }
            }
    }
}

class DesignSystemTest {

    @Test
    fun `given public design system composables, when checking parameters, then all expose a modifier`() {
        Konsist.scopeFromProduction().functions()
            .withAnnotationNamed("Composable")
            .filter { it.packagee?.name?.startsWith("$ROOT.core.ui.design") == true && !it.hasPrivateModifier }
            .assertTrue(additionalMessage = "Componentes de core/ui/design recebem `modifier: Modifier = Modifier` como primeiro parâmetro opcional.") { fn ->
                fn.parameters.any { it.name == "modifier" && it.type.name == "Modifier" }
            }
    }

    @Test
    fun `given design system files, when checking imports, then none import feature code`() {
        Konsist.scopeFromProduction().files
            .filter { it.packagee?.name?.startsWith("$ROOT.core.ui.design") == true }
            .assertFalse(additionalMessage = "O design system não conhece features: receba dados primitivos ou um modelo próprio do DS (ex.: ItemListEntry).") { file ->
                file.imports.any { it.name.startsWith("$ROOT.") && !it.name.startsWith("$ROOT.core.") && it.name != "$ROOT.R" }
            }
    }
}

class TestConventionTest {

    @Test
    fun `given test functions, when checking names, then all follow given when then`() {
        Konsist.scopeFromTest().functions()
            .withAnnotationNamed("Test")
            .assertTrue(additionalMessage = "Nomeie como `given <contexto>, when <ação>, then <resultado>` (JVM) ou givenX_whenY_thenZ (androidTest).") {
                val n = it.name.lowercase()
                n.startsWith("given") && "when" in n && "then" in n
            }
    }
}
