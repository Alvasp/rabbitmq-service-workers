import { NextRequest } from "next/server";

export async function GET(request: NextRequest) {
    const params = request.nextUrl.searchParams
    const jobs = params.get('uuids')

    const result = await fetch(`http://localhost:8080?uuids=${jobs}`)
    const data = await result.json()

    return Response.json({ data });
}

export async function POST(request: Request) {
    const formData = await request.formData()

    const result = await fetch(`http://localhost:8080/`, {
        method: 'POST',
        body: formData
    })

    const data = await result.text()

    return Response.json({ uuid: data });
}
