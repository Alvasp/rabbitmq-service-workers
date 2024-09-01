import { useEffect, useState } from "react";

export type CountdownProps = {
    from: number,
    onCountDownEnd: () => Promise<any>
}
export default function Countdown({ from, onCountDownEnd }: CountdownProps) {
    const [value, setValue] = useState(from);

    useEffect(() => {
        setValue(from);
    }, [from])

    useEffect(() => {
        const interval = setInterval(() => {
            setValue((prevValue) => {
                if (prevValue > 1) {
                    return prevValue - 1;
                } else {
                    clearInterval(interval);
                    return 0;
                }
            });
        }, 1000);

        return () => clearInterval(interval); // Cleanup on unmount
    }, []);

    useEffect(() => {
        if (value === 0) {
            onCountDownEnd()
        }
    }, [value, onCountDownEnd]);

    return (
        <p className="ml-1 mr-1">{value}</p>
    )
}