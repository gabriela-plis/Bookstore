import { useRef } from "react";

type Props = {
    text: string,
    button: boolean,
    handleClick?: () => void
}

const Feedback = (props: Props) => {
    const {text, button, handleClick} = {...props};

    const feedbackElement = useRef<HTMLDivElement>(null);


    return ( 
        <div className="feedback" ref={feedbackElement}>
        <p className="feedback__text">
            {text} 
        </p>
        {button && <button className="btn btn--smaller btn--greater-border-radius btn--margin" onClick={handleClick}>x</button>}
        </div>
     );
}
 
export default Feedback;