import { useState } from "react";
import { Link } from "react-router-dom";
import PasswordInput from "./inputs/PasswordInput";

const LogInPage = () => {
    return ( 
        <main className="loginPage panel-wrapper">
            <LogInSection />
            <RegisterOptionSection />
        </main>
     );
}


const LogInSection = () => {
    const [ID, setID] = useState(0);
    const [password, setPassword] = useState('');

    return (
        <section className="loginPage__login-section panel-wrapper__panel--no-image-background">
            <h2 className="panel-wrapper__title">Log In to Your Account</h2>
            <form className="login-section__form">
                <p className="login-section__form-fields">
                    <input 
                        type="number"
                        required 
                        value={ID}
                        onChange={(e) => {setID(e.target.valueAsNumber)}}
                        placeholder="ID"
                    />
                    <PasswordInput state={password} setState={setPassword} isRequired={true} placeholder="Password" />
                </p>
                <button className="btn btn--login">Log In</button>
            </form>
        </section>
    )
}

const RegisterOptionSection = () => {
    return (
        <section className="loginPage__register-text-section panel-wrapper__panel--image-background">
            <h2 className="panel-wrapper__title">New to Bookstore?</h2>
            <p className="register-text-section__paragraph">Sing up and discover a great amount of new opportunities!</p>
            <Link to='/register' className="register-text-section__link">SIGN UP</Link>
        </section>
    )
}
 
export default LogInPage;