// // PersonalAlarmList.tsx
// import React from "react";
// import PersonalAlarm from "./PersonalAlarm";

// interface PersonalAlarmListProps {
//   alarms: { teamName: string; date: string; id: number }[];
//   onDelete: (id: number) => void;
// }

// const PersonalAlarmList: React.FC<PersonalAlarmListProps> = ({
//   alarms,
//   onDelete,
// }) => {
//   return (
//     <div>
//       {alarms.length > 0 ? (
//         alarms.map((alarm) => (
//           <PersonalAlarm
//             key={alarm.id}
//             teamName={alarm.teamName}
//             date={alarm.date}
//             onDelete={() => onDelete(alarm.id)}
//           />
//         ))
//       ) : (
//         <p>알람이 없습니다.</p>
//       )}
//     </div>
//   );
// };

// export default PersonalAlarmList;
