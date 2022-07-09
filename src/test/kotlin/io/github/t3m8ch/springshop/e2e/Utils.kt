import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers

fun isNotEmptyString() = CoreMatchers.`is`(CoreMatchers.not(Matchers.emptyString()))
