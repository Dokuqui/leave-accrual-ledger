package controllers

import play.api.mvc._
import javax.inject._
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat
import io.prometheus.client.hotspot.DefaultExports
import java.io.StringWriter

@Singleton
class MetricsController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {

  DefaultExports.initialize()

  def metrics() = Action {
    val writer = new StringWriter()
    TextFormat.write004(
      writer,
      CollectorRegistry.defaultRegistry.metricFamilySamples()
    )

    Ok(writer.toString).as(TextFormat.CONTENT_TYPE_004)
  }
}
