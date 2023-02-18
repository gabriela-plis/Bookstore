import React, { useEffect, useState } from 'react';
import './App.css';
import Header from './Header';
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import Homepage from './Homepage';
import LogInPage from './LogInPage';
import RegisterPage from './RegisterPage';

function App() {
  const [sessionIsActive, setSessionIsActive] = useState(false);
  const location = useLocation();

  useEffect(() => {
    console.log("Im in session menagment")
    if (sessionStorage.getItem("id") === null) {
        setSessionIsActive(false)
    } else {
        setSessionIsActive(true)
    }
},[location])

  return (
    <div className="App">
      <Header sessionIsActive={sessionIsActive}/>
      <Routes>
        <Route path="/" element={<Homepage />}></Route>
        <Route path="/logIn" element={<LogInPage />}></Route>
        <Route path="/register" element={<RegisterPage />}></Route>
      </Routes>
    </div>
  );
}

export default App;
