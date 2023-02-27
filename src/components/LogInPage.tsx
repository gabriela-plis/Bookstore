import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import PasswordInput from "../reusable-components/PasswordInput";
import TextInput from "../reusable-components/TextInput";

const LogInPage = () => {
    return ( 
        <main className="loginPage panel-wrapper">
            <LogInSection />
            <RegisterOptionSection />
        </main>
     );
}


const LogInSection = () => {
    const [loginData, setLoginData] = useState({
        email: '',
        password: ''
    });

    const [wrongData, setWrongData] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

                //Send data to backend and get true or false answer
        // const userLogInData = { email, password };
        // fetch('http://localhost:8000/login', {
        //     method: "POST",
        //     headers: { "Content-Type": "application/json" },
        //     body: JSON.stringify(userLogInData)
        // })
        // .then( resp => { console.log(resp) });
 
        //Temporarily I use GET METHOD

        const correctId = 1;
        const incorrectId = 5;

        fetch('http://localhost:8000/users/' + correctId)
        .then(resp => resp.json())
        .then(data => {
            console.log(data)
            if (Object.keys(data).length === 0) {
                setWrongData(true);

            } else {
                sessionStorage.setItem("id", data.id)
                sessionStorage.setItem("name", data.firstName)

                navigate('/')
            }
        })
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setLoginData(prevState => (
            {
                ...prevState,
                [e.target.name]: e.target.value
            }
        )) 
        
    }

    return (
        <section className="loginPage__login-section panel-wrapper__panel--no-image-background">
            <h2 className="panel-wrapper__title">Log In to Your Account</h2>
            <form className="form" onSubmit={handleSubmit}>
                <div className="form__fields">
                    <TextInput name="email" state={loginData.email} setState={handleChange} isRequired placeholder="Email"/>
                    <PasswordInput name="password" state={loginData.password} setState={handleChange} placeholder="Password" />
                    {wrongData && <p className="incorrect-data-text">Incorrect email or password</p>}
                </div>
                <button className="btn btn--login">Log In</button>
            </form>
        </section>
    )
}

const RegisterOptionSection = () => {
    return (
        <section className="loginPage__register-text-section panel-wrapper__panel--image-background">
            <h2 className="panel-wrapper__title">New to Bookstore?</h2>
            <p className="panel-wrapper__paragraph">Sing up and discover a great amount of new opportunities!</p>
            <Link to='/register' className="panel-wrapper__link">SIGN UP</Link>
        </section>
    )
}
 
export default LogInPage;