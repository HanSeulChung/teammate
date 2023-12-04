import { useState } from 'react'
import './App.css'
import { BrowserRouter } from "react-router-dom"
import Router from './router/router'
import Header from './components/common/Header'
import Footer from './components/common/Footer'

function App() {
    const [count, setCount] = useState(0)

    return (
        <BrowserRouter>
            <Header />
            <section>
                <Router />
            </section>
            <Footer />
        </BrowserRouter>
        
    )
}

export default App
