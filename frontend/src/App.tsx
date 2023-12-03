import "./App.css";
import { BrowserRouter } from "react-router-dom";
import Router from "./router/router";

function App() {
  return (
    <BrowserRouter>
      {/* <Navbar /> */}
      <section>
        <Router />
      </section>
      {/* <Footer /> */}
    </BrowserRouter>
  );
}

export default App;
