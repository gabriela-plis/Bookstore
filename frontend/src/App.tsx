import React, { useEffect, useState } from 'react';
import './App.css';
import Header from './components/Header';
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import Homepage from './components/home-page/Homepage';
import LogInPage from './components/authentication-pages/LogInPage';
import RegisterPage from './components/authentication-pages/RegisterPage';
import UserPage from './components/user-page/UserPage';

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
        <Route path="/" element={<Homepage sessionIsActive={sessionIsActive} displayLogoutText={displayLogoutText} />}></Route>
        <Route path="/logIn" element={<LogInPage />}></Route>
        <Route path="/register" element={<RegisterPage />}></Route>
        <Route path="/user/borrows" element={<UserPage setDisplayLogoutText={setDisplayLogoutText}/>}></Route>
        <Route path="/user/details" element={<UserPage setDisplayLogoutText={setDisplayLogoutText}/>}></Route>
        <Route path="/user/settings" element={<UserPage setDisplayLogoutText={setDisplayLogoutText}/>}></Route>
        <Route path="/user/add-book" element={<UserPage setDisplayLogoutText={setDisplayLogoutText}/>}></Route>
        <Route path="/user/remove-book" element={<UserPage setDisplayLogoutText={setDisplayLogoutText}/>}></Route>
      </Routes>
    </div>
  );
}

export default App;
