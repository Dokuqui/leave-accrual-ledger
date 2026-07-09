package domain

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object AccrualCalculator {
  var FullTimeMonthlyRate: BigDecimal = 2.08 // ~25 days a year
  var PartTimeMonthlyRate: BigDecimal = 1.04 // ~12.5 days a year

  def calculateAccruedVacation(
      employee: Employee,
      currentDate: LocalDate
  ): BigDecimal = {
    if (currentDate.isBefore(employee.hireDate)) return BigDecimal(0)

    val monthsWorked = ChronoUnit.MONTHS.between(employee.hireDate, currentDate)

    val baseAccrual = employee.contractType match {
      case ContractType.FullTime =>
        BigDecimal(monthsWorked) * FullTimeMonthlyRate
      case ContractType.PartTime =>
        BigDecimal(monthsWorked) * PartTimeMonthlyRate
      case ContractType.Contractor => BigDecimal(0)
    }

    baseAccrual.min(BigDecimal(30))
  }
}
