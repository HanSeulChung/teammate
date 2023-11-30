import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { BrowserRouter } from "react-router-dom"
import Router from './router/router'

function App() {
    const [count, setCount] = useState(0)

    return (
        <BrowserRouter>
            {/* <Navbar /> */}
            <section>
                <Router />
            </section>
            {/* <Footer /> */}
        </BrowserRouter>
        
    )
}

export default App
