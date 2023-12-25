import TeamCalender from "../components/Calendar/TeamCalender"
import CalendarCategory from "../components/Calendar/CalendarCategory"

const Calender = () => {
  return (
    <div className="flex min-h-screen flex-col items-center justify-between p-24">
      <div className="grid grid-cols-10">
        <div className="col-span-8">
          <TeamCalender />
        </div>
        <div className="ml-8 w-full mt-16 lg:h-1/2">
          <CalendarCategory />
        </div>
      </div>
    </div>

  )
}

export default Calender;