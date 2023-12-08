import React from "react";
import { Link } from "react-router-dom";

export default function HomeCreateTeamBtn() {
  return (
    <Link to="/teamcreateview">
      <button>+ 팀 생성하기</button>
    </Link>
  );
}
