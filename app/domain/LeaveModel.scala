package domain

import java.time.LocalDate
import java.util.UUID

case class Employee(
    id: UUID,
    firstName: String,
    lastName: String,
    hireDate: LocalDate,
    contractType: ContractType
)

sealed trait ContractType
object ContractType {
  case object FullTime extends ContractType
  case object PartTime extends ContractType
  case object Contractor extends ContractType
}

sealed trait LeaveType
object LeaveType {
  case object Vacation extends LeaveType
  case object Sick extends LeaveType
  case object Maternity extends LeaveType
}

sealed trait LeaveStatus
object LeaveStatus {
  case object Pending extends LeaveStatus
  case object Approved extends LeaveStatus
  case object Rejected extends LeaveStatus
}

case class LeaveRequest(
    id: UUID,
    employeeId: UUID,
    leaveType: LeaveType,
    startDate: LocalDate,
    endDate: LocalDate,
    status: LeaveStatus
)

case class LeaveBalance(
    employeeId: UUID,
    accruedDays: BigDecimal,
    takenDays: BigDecimal,
    pendingDays: BigDecimal
) {
  def availableDays: BigDecimal = accruedDays - takenDays - pendingDays
}
