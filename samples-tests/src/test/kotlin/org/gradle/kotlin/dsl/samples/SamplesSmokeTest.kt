package org.gradle.kotlin.dsl.samples

import org.gradle.kotlin.dsl.embeddedKotlinVersion
import org.gradle.kotlin.dsl.fixtures.AbstractIntegrationTest

import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not

import org.junit.Assert.assertThat
import org.junit.Assume.assumeThat
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import java.io.File


@RunWith(Parameterized::class)
class SamplesSmokeTest(
    private val sampleName: String,
    private val sampleDir: File) : AbstractIntegrationTest() {

    companion object {
        @Parameterized.Parameters(name = "{0}")
        @JvmStatic
        fun testCases(): Iterable<Array<Any>> =
            samplesRootDir.listFiles().filter { it.isDirectory }.map { arrayOf(it.name, it) }
    }

    @Before
    fun populateProjectRootWithSample() {
        ignoreAndroidSampleUnlessAndroidHomeIsSet()
        copySampleProject(from = sampleDir, to = projectRoot)
    }

    @Test
    fun `tasks task succeeds on `() {
        assumeThat(
            "Require published `kotlin-dsl` plugin depending on Kotlin 1.2",
            sampleName,
            not(equalTo("multi-kotlin-project-with-buildSrc")))

        build("tasks")
    }

    @Test
    fun `uses the right Kotlin Gradle Plugin version on `() {

        assumeThat(
            "Require published `kotlin-dsl` plugin depending on Kotlin 1.2",
            listOf("model-rules", "multi-kotlin-project-with-buildSrc", "multi-project-with-buildSrc", "project-with-buildSrc"),
            not(hasItem(sampleName)))

        val projectPaths = listOf(":") + listSubProjectPaths().map { "$it:" }
        val projectBuilds = projectPaths.map { buildSpec("${it}buildEnvironment") }
        val buildsToCheck =
            if (File(sampleDir, "buildSrc").isDirectory) {
                projectBuilds + listOf(buildSpec("-p", "buildSrc", "buildEnvironment"))
            } else
                projectBuilds

        val foundKotlinGradlePlugin = buildsToCheck.map(this::assertKotlinGradlePluginVersion)

        // Mark that test as ignored if not using the kotlin-gradle-plugin
        assumeTrue(foundKotlinGradlePlugin.any { it })
    }

    private
    fun ignoreAndroidSampleUnlessAndroidHomeIsSet() {
        if (sampleName.contains("android")) {
            assumeTrue(System.getenv().containsKey("ANDROID_HOME"))
        }
    }

    private
    fun buildSpec(vararg arguments: String) = arguments

    private
    fun assertKotlinGradlePluginVersion(buildSpec: Array<out String>): Boolean =
        build("-q", *buildSpec).run {
            if (output.contains(":kotlin-gradle-plugin:")) {
                assertThat(output, containsString(":kotlin-gradle-plugin:$embeddedKotlinVersion"))
                true
            } else
                false
        }

    private
    val extractSubProjectPaths = Regex("""Project '(:.*)'""")

    private
    fun listSubProjectPaths() =
        build("projects", "-q").output.lines()
            .mapNotNull { extractSubProjectPaths.find(it)?.run { groupValues[1] } }
}
