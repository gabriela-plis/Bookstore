import React, { useEffect, useState } from 'react';
import Header from './components/header/Header';
import { Route, Routes, useLocation } from 'react-router-dom';
import Homepage from './components/home-page/Homepage';
import LogInPage from './components/authentication-pages/LogInPage';
import RegisterPage from './components/authentication-pages/RegisterPage';
import UserPage from './components/user-page/UserPage';
import NotFoundPage from './components/not-found-page/NotFoundPage';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [renderLogoutText, setRenderLogoutText] = useState(false);
  const location = useLocation();

  useEffect(() => {

      if (sessionStorage.getItem("id") !== null) {
          setIsAuthenticated(true)
      } 

      if (location.pathname !== '/') {
        setRenderLogoutText(false);
      }

  },[location])

  const userPage = React.cloneElement(<UserPage setRenderLogoutText={setRenderLogoutText} />);

  return (
    <div className="app">
      <div className="app__background">
        <div className="app__page-container">
          <Header isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated} setRenderLogoutText={setRenderLogoutText}/>
          <Routes>
            <Route path="/" element={<Homepage isAuthenticated={isAuthenticated} renderLogoutText={renderLogoutText} setRenderLogoutText={setRenderLogoutText} />}></Route>
            <Route path="/logIn" element={<LogInPage setIsAuthenticated={setIsAuthenticated} />}></Route>
            <Route path="/register" element={<RegisterPage />}></Route>
            <Route path="/user/borrows" element={userPage}></Route>
            <Route path="/user/details" element={userPage}></Route>
            <Route path="/user/settings" element={userPage}></Route>
            <Route path="/user/add-book" element={userPage}></Route>
            <Route path="/user/remove-book" element={userPage}></Route>
            <Route path="*" element={<NotFoundPage />}></Route>
          </Routes>
        </div>
      </div>
     </div>
  );
}

export default App;
