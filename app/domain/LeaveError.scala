package domain

import java.time.LocalDate

sealed trait LeaveError extends Exception {
  def message: String
}

object LeaveError {
  case class InsufficientBalance(available: BigDecimal, requested: BigDecimal)
      extends LeaveError {
    val message =
      s"Cannot request $requested days. Only $available days available."
  }

  case class InvalidDateRange(start: LocalDate, end: LocalDate)
      extends LeaveError {
    val message = s"End date $end must be after start date $start."
  }

  case class EmployeeNotFound(id: java.util.UUID) extends LeaveError {
    val message = s"Employee with ID $id was not found."
  }
}
