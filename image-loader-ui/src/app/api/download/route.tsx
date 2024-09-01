import { NextRequest, NextResponse } from "next/server";

export async function GET(request: NextRequest) {
    const params = request.nextUrl.searchParams
    const file = params.get('file')

    const result = await fetch(`http://localhost:8080/file/${file}`)
    const content = await result.blob();

    const response = new NextResponse(content.stream(), {
        headers: {
            'Content-Type': 'image/png',
            'Content-Disposition': `attachment; filename="${file}"`,
        },
    });

    return response;
}
