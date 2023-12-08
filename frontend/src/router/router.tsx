import { Routes, Route } from "react-router-dom";

import Calender from "../views/Calender";
import SignIn from "../components/SignIn";
import SignUp from "../components/SignUp";
import TextEditorView from "../views/TextEditorView";
import MyComponent from "../components/WebSocketTest";
import TextEditorViewV2 from "../views/TextEditorViewV2";

const Router = () => {
  return (
    <Routes>
      {/* <Route path='*' element={<Error />} /> */}
      <Route path="/캘린더" element={<Calender />} />
      <Route path="text-editor" element={<TextEditorView />} />
      <Route path="text-editorV2" element={<TextEditorViewV2 />} />
      <Route path="test" element={<TextEditorViewV2 />} />
      <Route path="test" element={<MyComponent />} />
    </Routes>
  );
};

export default Router;
