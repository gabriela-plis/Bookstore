import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import TextInput from "../../reusable-components/TextInput";
import PasswordInput from "../../reusable-components/PasswordInput";
import RegisteredUser from "../../DTO/RegisteredUserDTO"
import { USER_URL } from "../../constants/constants";

const RegisterPage = () => {
    return ( 
    <main className="registerPage panel-wrapper">
        <WelcomeSection />
        <RegisterSection />
    </main> 
    );
}

const RegisterSection = () => {

    const [user, setUser] = useState<RegisteredUser>({
        firstName: "",
        lastName: "",
        phone: "",
        email: "",
        password: "",
        employee: false
    });


    const [wrongData, setWrongData] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        fetch(USER_URL + '/register', {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        })
        .then( resp => {
            if (resp.ok) {
                navigate('/');
            } else {
                setWrongData(true);
            }
        });
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUser(prevState => (
            {
                ...prevState,
                [e.target.name]: e.target.value
            }
        ))    
    }

    return (
        <section className="registerPage__register-section panel-wrapper__panel--no-image-background">
            <h2 className="panel-wrapper__title">Create Account</h2>
            <form className="form" onSubmit={handleSubmit}>
                <p className="form__fields">
                    <TextInput name="firstName" state={user.firstName} setState={handleChange} isRequired placeholder="First name"/>
                    <TextInput name="lastName" state={user.lastName} setState={handleChange} isRequired placeholder="Last name"/>
                    <TextInput name="email" state={user.email} setState={handleChange} isRequired placeholder="Email" />
                    <TextInput name="phone" state={user.phone} setState={handleChange} isRequired={false} placeholder="Phone"/>
                    <PasswordInput name="password" state={user.password} setState={handleChange} placeholder="Password"/>
                </p>
                {wrongData && <div className="error-text">Something goes wrong, try again</div>}
                <button className="btn btn--pink btn--greater btn--greater-border-radius">Sign In</button>
            </form>
        </section>
    )
}

const WelcomeSection = () => {
    return (
        <section className="registerPage__welcome-section panel-wrapper__panel--image-background">
            <h2 className="panel-wrapper__title">Welcome!</h2>
            <p className="panel-wrapper__paragraph panel-wrapper__paragraph--long-text">A trip to the bookstore is the ultimate exercise in empathy. Within it you will find endless opportunities to see and understand the world from someone else’s point of view.</p>
            <Link to='/' className="panel-wrapper__link">SEE OUR BOOKS</Link>
        </section>
    )
}


export default RegisterPage;