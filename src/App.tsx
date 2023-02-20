import React, { useEffect, useState } from 'react';
import './App.css';
import Header from './Header';
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import Homepage from './Homepage';
import LogInPage from './LogInPage';
import RegisterPage from './RegisterPage';
import CustomerPage from './CustomerPage';

function App() {
  const [sessionIsActive, setSessionIsActive] = useState(false);
  const [displayLogoutText, setDisplayLogoutText] = useState(false);
  const location = useLocation();

  useEffect(() => {
    if (sessionStorage.getItem("id") === null) {
        setSessionIsActive(false)
    } else {
        setSessionIsActive(true)
    }

    if (location.pathname !== '/') {
      setDisplayLogoutText(false);
    }

},[location])

  return (
    <div className="App">
      <Header sessionIsActive={sessionIsActive} setDisplayLogoutText={setDisplayLogoutText}/>
      <Routes>
        <Route path="/" element={<Homepage displayLogoutText={displayLogoutText} />}></Route>
        <Route path="/logIn" element={<LogInPage />}></Route>
        <Route path="/register" element={<RegisterPage />}></Route>
        <Route path="/customer/borrows" element={<CustomerPage setDisplayLogoutText={setDisplayLogoutText}/>}></Route>
        <Route path="/customer/details" element={<CustomerPage setDisplayLogoutText={setDisplayLogoutText}/>}></Route>
        <Route path="/customer/settings" element={<CustomerPage setDisplayLogoutText={setDisplayLogoutText}/>}></Route>
      </Routes>
    </div>
  );
}

export default App;
