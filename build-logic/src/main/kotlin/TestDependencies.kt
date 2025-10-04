import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.test(project: Project) {
    "testImplementation"(project.libraries.findLibrary("junit").get())
    "testImplementation"(project.libraries.findLibrary("junit").get())
    "androidTestImplementation"(project.libraries.findLibrary("androidx-junit").get())
    "androidTestImplementation"(project.libraries.findLibrary("androidx-espresso-core").get())
}